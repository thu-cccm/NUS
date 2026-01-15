#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
批量删除代码注释并更新作者信息
"""
import os
import re
from pathlib import Path

def remove_java_comments(content):
    """删除Java文件中的注释"""
    # 先处理多行注释（包括文档注释）/* ... */ 和 /** ... */
    # 使用非贪婪匹配，但要避免匹配到字符串中的/*
    lines = content.split('\n')
    result = []
    in_multiline_comment = False
    comment_start = None
    
    for i, line in enumerate(lines):
        if in_multiline_comment:
            # 检查是否结束多行注释
            if '*/' in line:
                end_idx = line.find('*/')
                # 保留注释结束后的内容
                remaining = line[end_idx + 2:].strip()
                if remaining:
                    result.append(remaining)
                in_multiline_comment = False
            # 否则跳过整行（注释中）
            continue
        
        # 检查是否开始多行注释
        if '/*' in line:
            start_idx = line.find('/*')
            # 检查是否在同一行结束
            if '*/' in line[start_idx:]:
                end_idx = line.find('*/', start_idx)
                # 保留注释前后的内容
                before = line[:start_idx].strip()
                after = line[end_idx + 2:].strip()
                if before or after:
                    result.append((before + ' ' + after).strip())
                continue
            else:
                # 多行注释开始
                before = line[:start_idx].strip()
                if before:
                    result.append(before)
                in_multiline_comment = True
                continue
        
        # 处理单行注释 //
        if '//' in line:
            comment_idx = line.find('//')
            # 检查是否在字符串中（简单检查，不完美但够用）
            before_comment = line[:comment_idx]
            # 如果引号数量是偶数，说明不在字符串中
            if before_comment.count('"') % 2 == 0 and before_comment.count("'") % 2 == 0:
                remaining = before_comment.strip()
                if remaining:
                    result.append(remaining)
                continue
        
        # 普通行，直接添加
        if line.strip():
            result.append(line)
        else:
            result.append('')
    
    return '\n'.join(result)

def remove_js_comments(content):
    """删除JavaScript/TypeScript文件中的注释"""
    lines = content.split('\n')
    result = []
    in_multiline_comment = False
    
    for line in lines:
        if in_multiline_comment:
            if '*/' in line:
                end_idx = line.find('*/')
                remaining = line[end_idx + 2:].strip()
                if remaining:
                    result.append(remaining)
                in_multiline_comment = False
            continue
        
        if '/*' in line:
            start_idx = line.find('/*')
            if '*/' in line[start_idx:]:
                end_idx = line.find('*/', start_idx)
                before = line[:start_idx].strip()
                after = line[end_idx + 2:].strip()
                if before or after:
                    result.append((before + ' ' + after).strip())
                continue
            else:
                before = line[:start_idx].strip()
                if before:
                    result.append(before)
                in_multiline_comment = True
                continue
        
        if '//' in line:
            comment_idx = line.find('//')
            before_comment = line[:comment_idx]
            if before_comment.count('"') % 2 == 0 and before_comment.count("'") % 2 == 0:
                remaining = before_comment.strip()
                if remaining:
                    result.append(remaining)
                continue
        
        if line.strip():
            result.append(line)
        else:
            result.append('')
    
    return '\n'.join(result)

def remove_vue_comments(content):
    """删除Vue文件中的注释"""
    # 删除HTML注释 <!-- ... -->
    content = re.sub(r'<!--.*?-->', '', content, flags=re.DOTALL)
    
    # 删除JavaScript注释
    content = remove_js_comments(content)
    
    return content

def remove_sql_comments(content):
    """删除SQL文件中的注释"""
    lines = content.split('\n')
    result = []
    in_multiline_comment = False
    
    for line in lines:
        if in_multiline_comment:
            if '*/' in line:
                end_idx = line.find('*/')
                remaining = line[end_idx + 2:].strip()
                if remaining:
                    result.append(remaining)
                in_multiline_comment = False
            continue
        
        if '/*' in line:
            start_idx = line.find('/*')
            if '*/' in line[start_idx:]:
                end_idx = line.find('*/', start_idx)
                before = line[:start_idx].strip()
                after = line[end_idx + 2:].strip()
                if before or after:
                    result.append((before + ' ' + after).strip())
                continue
            else:
                before = line[:start_idx].strip()
                if before:
                    result.append(before)
                in_multiline_comment = True
                continue
        
        # SQL单行注释 --
        if '--' in line:
            comment_idx = line.find('--')
            remaining = line[:comment_idx].strip()
            if remaining:
                result.append(remaining)
                continue
        
        if line.strip():
            result.append(line)
        else:
            result.append('')
    
    return '\n'.join(result)

def remove_css_comments(content):
    """删除CSS/SCSS文件中的注释"""
    return remove_js_comments(content)

def update_author(content):
    """更新作者信息为 maoshun tian"""
    # 替换@author标签
    content = re.sub(r'@author\s+[^\n]*', '@author maoshun tian', content, flags=re.IGNORECASE)
    
    # 替换JSON中的author字段
    content = re.sub(r'"author"\s*:\s*"[^"]*"', '"author": "maoshun tian"', content, flags=re.IGNORECASE)
    content = re.sub(r"'author'\s*:\s*'[^']*'", "'author': 'maoshun tian'", content, flags=re.IGNORECASE)
    
    return content

def clean_empty_lines(content):
    """清理多余的空行"""
    # 将多个连续空行替换为最多两个空行
    content = re.sub(r'\n\s*\n\s*\n+', '\n\n', content)
    # 删除文件开头的空行
    content = re.sub(r'^\n+', '', content)
    # 删除文件结尾的多个空行，保留最多一个
    content = re.sub(r'\n+$', '\n', content)
    return content

def process_file(file_path):
    """处理单个文件"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # 根据文件类型删除注释
        ext = file_path.suffix.lower()
        if ext == '.java':
            content = remove_java_comments(content)
        elif ext in ['.js', '.ts', '.jsx', '.tsx']:
            content = remove_js_comments(content)
        elif ext == '.vue':
            content = remove_vue_comments(content)
        elif ext == '.sql':
            content = remove_sql_comments(content)
        elif ext in ['.scss', '.css']:
            content = remove_css_comments(content)
        elif ext in ['.xml', '.yml', '.yaml', '.properties', '.vm']:
            # 对于这些文件，只更新作者信息，不删除注释（可能包含重要配置）
            pass
        
        # 更新作者信息
        content = update_author(content)
        
        # 清理多余空行
        content = clean_empty_lines(content)
        
        # 如果内容有变化，写回文件
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            return True
        return False
    except Exception as e:
        print(f"处理文件 {file_path} 时出错: {e}")
        return False

def main():
    """主函数"""
    base_dir = Path(__file__).parent
    
    # 需要处理的文件扩展名
    extensions = ['.java', '.js', '.ts', '.vue', '.sql', '.scss', '.css', 
                  '.xml', '.yml', '.yaml', '.properties', '.vm', '.json']
    
    # 需要排除的目录
    exclude_dirs = {'node_modules', '.git', 'target', 'dist', 'build', 
                   '.idea', '.vscode', '__pycache__', '.mvn'}
    
    processed_count = 0
    error_count = 0
    
    for root, dirs, files in os.walk(base_dir):
        # 排除不需要的目录
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        
        for file in files:
            file_path = Path(root) / file
            if file_path.suffix.lower() in extensions:
                if process_file(file_path):
                    processed_count += 1
                    print(f"已处理: {file_path}")
                else:
                    error_count += 1
    
    print(f"\n处理完成！")
    print(f"成功处理: {processed_count} 个文件")
    print(f"处理失败: {error_count} 个文件")

if __name__ == '__main__':
    main()
